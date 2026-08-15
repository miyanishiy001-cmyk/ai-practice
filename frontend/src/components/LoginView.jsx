import React, { useState } from 'react'

export default function LoginView({ onLoginSuccess }) {
  const [isRegister, setIsRegister] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [nickname, setNickname] = useState('')
  const [errorMsg, setErrorMsg] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!username.trim() || !password.trim()) {
      setErrorMsg('ユーザー名とパスワードを入力してください')
      return
    }

    setLoading(true)
    setErrorMsg('')

    const endpoint = isRegister ? '/api/user/register' : '/api/user/login'
    const payload = isRegister 
      ? { username: username.trim(), password: password.trim(), nickname: nickname.trim() || username.trim() }
      : { username: username.trim(), password: password.trim() }

    try {
      const res = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      })

      const data = await res.json()

      if (res.ok) {
        onLoginSuccess(data)
      } else {
        setErrorMsg(data.error || 'エラーが発生しました')
      }
    } catch (err) {
      console.error('通信エラー:', err)
      setErrorMsg('サーバーとの通信に失敗しました')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '60vh', width: '100%' }}>
      <div className="glass-card" style={{ maxWidth: '420px', width: '100%', padding: '1.5rem 1.25rem' }}>
        
        <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
          <span style={{ fontSize: '3.5rem' }}>🐣</span>
          <h2 className="logo-title" style={{ fontSize: '1.8rem', marginTop: '0.5rem' }}>AI育成図鑑</h2>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginTop: '0.3rem' }}>
            {isRegister ? '新規プレイヤーアカウントを作成' : 'ログインして育成を再開'}
          </p>
        </div>

        {/* タブ切り替え */}
        <div className="tab-nav" style={{ marginBottom: '1.5rem', width: '100%' }}>
          <button
            className={`tab-btn ${!isRegister ? 'active' : ''}`}
            style={{ flex: 1, justifyContent: 'center' }}
            onClick={() => { setIsRegister(false); setErrorMsg('') }}
          >
            ログイン
          </button>
          <button
            className={`tab-btn ${isRegister ? 'active' : ''}`}
            style={{ flex: 1, justifyContent: 'center' }}
            onClick={() => { setIsRegister(true); setErrorMsg('') }}
          >
            新規アカウント作成
          </button>
        </div>

        {errorMsg && (
          <div style={{
            background: 'rgba(239, 68, 68, 0.15)',
            border: '1px solid rgba(239, 68, 68, 0.4)',
            color: '#fca5a5',
            padding: '0.8rem',
            borderRadius: '10px',
            fontSize: '0.85rem',
            marginBottom: '1rem',
            textAlign: 'center'
          }}>
            ⚠️ {errorMsg}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '0.3rem' }}>
              ユーザーID (英数字)
            </label>
            <input
              type="text"
              className="action-textarea"
              style={{ minHeight: 'auto', padding: '0.75rem 1rem' }}
              placeholder="例: player1"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              disabled={loading}
            />
          </div>

          <div>
            <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '0.3rem' }}>
              パスワード
            </label>
            <input
              type="password"
              className="action-textarea"
              style={{ minHeight: 'auto', padding: '0.75rem 1rem' }}
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
            />
          </div>

          {isRegister && (
            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: 'block', marginBottom: '0.3rem' }}>
                プレイヤー表示名 (ニックネーム)
              </label>
              <input
                type="text"
                className="action-textarea"
                style={{ minHeight: 'auto', padding: '0.75rem 1rem' }}
                placeholder="例: たろう"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                disabled={loading}
              />
            </div>
          )}

          <button type="submit" className="submit-btn" style={{ width: '100%', marginTop: '0.5rem' }} disabled={loading}>
            {loading ? '処理中...' : isRegister ? 'アカウントを作成してゲームを始める 🐣' : 'ログインしてプレイする 🚀'}
          </button>
        </form>
      </div>
    </div>
  )
}
