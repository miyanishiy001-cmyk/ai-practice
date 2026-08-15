import React, { useState, useEffect } from 'react'

export default function DexView({ user }) {
  const [dexList, setDexList] = useState([])
  const [loading, setLoading] = useState(true)
  const [selectedDex, setSelectedDex] = useState(null)

  useEffect(() => {
    if (user?.id) {
      fetchDexList(user.id)
    }
  }, [user])

  const fetchDexList = async (userId) => {
    try {
      const res = await fetch(`/api/dex/${userId}`)
      if (res.ok) {
        const data = await res.json()
        setDexList(data)
      }
    } catch (err) {
      console.error('図鑑データの取得エラー:', err)
    } finally {
      setLoading(false)
    }
  }

  // 1〜30までの図鑑スロットを作成
  const slots = Array.from({ length: 30 }, (_, i) => i + 1)

  // 種族番号ごとにまとめる
  const dexMap = {}
  dexList.forEach(item => {
    if (!dexMap[item.characterNumber]) {
      dexMap[item.characterNumber] = []
    }
    dexMap[item.characterNumber].push(item)
  })

  return (
    <div className="glass-card" style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div>
        <h2 style={{ fontSize: '1.4rem' }}>📖 キャラクター図鑑コレクション (全30種)</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginTop: '0.2rem' }}>
          これまでに育てて解放したキャラクターたち。クリックするとAIが生成したオリジナルストーリーを閲覧できます。
        </p>
      </div>

      {loading ? (
        <p>図鑑を読み込んでいます...</p>
      ) : (
        <div className="dex-grid">
          {slots.map(num => {
            const history = dexMap[num]
            const isUnlocked = history && history.length > 0
            const latestChar = isUnlocked ? history[0] : null

            return (
              <div
                key={num}
                onClick={() => isUnlocked && setSelectedDex(latestChar)}
                style={{
                  background: isUnlocked ? 'rgba(99, 102, 241, 0.15)' : 'rgba(255, 255, 255, 0.03)',
                  border: isUnlocked ? '1px solid rgba(99, 102, 241, 0.4)' : '1px solid rgba(255, 255, 255, 0.06)',
                  borderRadius: '16px',
                  padding: '1rem 0.5rem',
                  textAlign: 'center',
                  cursor: isUnlocked ? 'pointer' : 'default',
                  transition: 'all 0.2s ease'
                }}
              >
                <div style={{ fontSize: '2.2rem', marginBottom: '0.3rem' }}>
                  {isUnlocked ? '👾' : '❓'}
                </div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  No.{num}
                </div>
                <div style={{ fontSize: '0.85rem', fontWeight: '700', marginTop: '0.2rem', color: isUnlocked ? '#fff' : '#64748b' }}>
                  {isUnlocked ? latestChar.characterName : '？？？？'}
                </div>
                {isUnlocked && (
                  <div style={{ fontSize: '0.7rem', color: '#f59e0b', marginTop: '0.3rem' }}>
                    {history.length} 世代記録
                  </div>
                )}
              </div>
            )
          })}
        </div>
      )}

      {/* 図鑑詳細モーダル */}
      {selectedDex && (
        <div className="modal-overlay" onClick={() => setSelectedDex(null)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <span style={{ fontSize: '3.5rem' }}>👾</span>
            <div className="modal-char-name">
              No.{selectedDex.characterNumber} {selectedDex.characterName}
            </div>
            <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: '1rem' }}>
              第 {selectedDex.generation} 世代の育成記録
            </p>

            <div className="modal-dex-desc">
              <strong>📖 AI生成図鑑テキスト:</strong>
              <p style={{ marginTop: '0.5rem' }}>{selectedDex.description}</p>
            </div>

            {selectedDex.historySummary && (
              <div className="modal-dex-desc" style={{ fontSize: '0.85rem' }}>
                <strong>📜 育成時の10回分の行動履歴:</strong>
                <pre style={{ marginTop: '0.5rem', whiteSpace: 'pre-wrap', fontFamily: 'inherit', color: '#94a3b8' }}>
                  {selectedDex.historySummary}
                </pre>
              </div>
            )}

            <button
              className="submit-btn"
              style={{ width: '100%', marginTop: '1rem' }}
              onClick={() => setSelectedDex(null)}
            >
              閉じる
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
