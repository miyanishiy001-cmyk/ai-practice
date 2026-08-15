import React, { useState, useEffect } from 'react'

const STAGE_CONFIG = {
  EGG: { name: 'タマゴ', icon: '🥚', color: '#fcd34d' },
  BABY: { name: '幼年期', icon: '🐣', color: '#60a5fa' },
  CHILD: { name: '成長期', icon: '🐥', color: '#34d399' },
  ADULT: { name: '成熟期', icon: '🦅', color: '#a78bfa' }
}

const STAT_CONFIG = [
  { key: 'hp', name: 'HP (体力)', color: 'var(--stat-hp)' },
  { key: 'atk', name: 'ATK (攻撃)', color: 'var(--stat-atk)' },
  { key: 'def', name: 'DEF (防御)', color: 'var(--stat-def)' },
  { key: 'str', name: 'STR (筋力)', color: 'var(--stat-str)' },
  { key: 'intStat', name: 'INT (知力)', color: 'var(--stat-int)' },
  { key: 'chm', name: 'CHM (魅力)', color: 'var(--stat-chm)' },
  { key: 'spd', name: 'SPD (素早さ)', color: 'var(--stat-spd)' },
]

export default function NurtureView({ onCharacterUpdate, user }) {
  const [character, setCharacter] = useState(null)
  const [actionText, setActionText] = useState('')
  const [loading, setLoading] = useState(false)
  const [lastResult, setLastResult] = useState(null)
  const [evolutionModalData, setEvolutionModalData] = useState(null)

  // 初期ロード：現在のキャラクター情報を取得
  useEffect(() => {
    if (user?.id) {
      fetchCurrentCharacter(user.id)
    }
  }, [user])

  const fetchCurrentCharacter = async (userId) => {
    try {
      const res = await fetch(`/api/nurture/current/${userId}`)
      if (res.ok) {
        const data = await res.json()
        if (!data) {
          // キャラクターが存在しない場合は初期化
          setCharacter(null)
          return
        }
        setCharacter(data)
        if (onCharacterUpdate && data) {
          onCharacterUpdate(data)
        }
      } else {
        // ユーザーがサーバーに存在しない場合はキャッシュを消去して再ログイン
        localStorage.removeItem('ai_game_user')
        window.location.reload()
      }
    } catch (err) {
      console.error('キャラクターデータの取得に失敗しました:', err)
    }
  }

  // 行動の送信処理
  const handleSubmitAction = async (e) => {
    e.preventDefault()
    if (!actionText.trim() || loading || !user?.id) return

    setLoading(true)
    try {
      const res = await fetch('/api/nurture/action', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: user.id, actionText: actionText.trim() })
      })

      if (res.ok) {
        const data = await res.json()
        setLastResult(data)
        setCharacter(data.currentCharacter)

        if (onCharacterUpdate && data.currentCharacter) {
          onCharacterUpdate(data.currentCharacter)
        }

        // 10回目の進化時モーダル表示
        if (data.evolved) {
          setEvolutionModalData({
            number: data.characterNumber,
            name: data.characterName,
            description: data.dexDescription
          })
        }

        setActionText('')
      }
    } catch (err) {
      console.error('行動の送信エラー:', err)
    } finally {
      setLoading(false)
    }
  }

  if (!character) {
    return (
      <div className="glass-card" style={{ textAlign: 'center', padding: '3rem' }}>
        <p>キャラクター情報を読み込んでいます...</p>
      </div>
    )
  }

  const stageInfo = STAGE_CONFIG[character.stage] || STAGE_CONFIG.EGG
  const maxStatVal = Math.max(100, character.hp, character.atk, character.def, character.str, character.intStat, character.chm, character.spd)

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      {/* 2カラムレイアウト */}
      <div className="nurture-grid">

        {/* 左カラム：キャラクタービジュアル＆現在レベル */}
        <div className="glass-card character-box">
          <div className="stage-avatar-wrapper">
            <span className="stage-avatar">{stageInfo.icon}</span>
          </div>

          <div>
            <span className="stage-name-badge" style={{ color: stageInfo.color }}>
              {stageInfo.name}
            </span>
          </div>

          <div className="action-progress-container">
            <div className="action-progress-header">
              <span>育成進捗 (行動回数)</span>
              <span><strong>{character.actionCount}</strong> / 10 回</span>
            </div>
            <div className="action-progress-bar">
              <div
                className="action-progress-fill"
                style={{ width: `${(character.actionCount / 10) * 100}%` }}
              />
            </div>
          </div>
        </div>

        {/* 右カラム：7因子ステータスゲージ */}
        <div className="glass-card">
          <h3 style={{ fontSize: '1.1rem', marginBottom: '1rem', color: 'var(--text-muted)' }}>
            📊 育成ステータス (経験値)
          </h3>
          <div className="stats-list">
            {STAT_CONFIG.map((stat) => {
              const val = character[stat.key] || 0
              const percentage = Math.min(100, (val / maxStatVal) * 100)
              const addedVal = lastResult?.addedStatus?.[stat.key]

              return (
                <div key={stat.key} className="stat-item">
                  <div className="stat-info">
                    <span style={{ color: stat.color }}>{stat.name}</span>
                    <span>
                      {val} Exp
                      {addedVal > 0 && (
                        <span style={{ color: '#34d399', marginLeft: '0.4rem', fontSize: '0.8rem' }}>
                          (+{addedVal})
                        </span>
                      )}
                    </span>
                  </div>
                  <div className="stat-bar-bg">
                    <div
                      className="stat-bar-fill"
                      style={{ width: `${percentage}%`, backgroundColor: stat.color }}
                    />
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      </div>

      {/* 行動入力 ＆ AIコメントカード */}
      <div className="glass-card input-card">
        <h3 style={{ fontSize: '1.1rem' }}>✏️ 今日の行動を入力して育成する</h3>

        <form onSubmit={handleSubmitAction} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <textarea
            className="action-textarea"
            placeholder="例: 今日は1時間部屋の掃除をして集中力を高めた！ / 森でドラゴンと戦った！"
            value={actionText}
            onChange={(e) => setActionText(e.target.value)}
            disabled={loading}
          />

          <button type="submit" className="submit-btn" disabled={loading || !actionText.trim()}>
            {loading ? '🤖 AIが行動を判定中...' : '🔥 行動を実行して成長させる'}
          </button>
        </form>

        {/* AIの一言フィードバック */}
        {lastResult?.aiComment && (
          <div className="ai-comment-box">
            <strong>🤖 AIのひとことフィードバック:</strong>
            <p style={{ marginTop: '0.3rem' }}>{lastResult.aiComment}</p>
          </div>
        )}
      </div>

      {/* 進化演出ポップアップモーダル */}
      {evolutionModalData && (
        <div className="modal-overlay">
          <div className="modal-content">
            <span style={{ fontSize: '4rem' }}>🎉</span>
            <h2 className="modal-title">祝！進化＆図鑑登録！</h2>
            <p style={{ color: 'var(--text-muted)' }}>10回の行動を経て新しい姿へ進化しました！</p>

            <div className="modal-char-name">
              No.{evolutionModalData.number} {evolutionModalData.name}
            </div>

            <div className="modal-dex-desc">
              <strong>📖 AIが生成したオリジナル解説文:</strong>
              <p style={{ marginTop: '0.5rem' }}>{evolutionModalData.description}</p>
            </div>

            <p style={{ fontSize: '0.85rem', color: '#fcd34d', marginBottom: '1.5rem' }}>
              ※ 図鑑に保存され、新しいタマゴから次の育成がスタートします！
            </p>

            <button
              className="submit-btn"
              style={{ width: '100%' }}
              onClick={() => setEvolutionModalData(null)}
            >
              次の世代の育成を始める 🥚
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
