import React from 'react'

export default function Header({ activeTab, setActiveTab, generation = 1, user, onLogout }) {
  return (
    <header className="glass-card header-card">
      <div className="logo-section">
        <span style={{ fontSize: '1.8rem' }}>🐣</span>
        <div>
          <h1 className="logo-title">AI育成図鑑</h1>
        </div>
      </div>

      <div className="header-controls">
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          {user && (
            <span style={{ fontSize: '0.85rem', color: '#c7d2fe', fontWeight: '600' }}>
              👤 {user.nickname || user.username}
            </span>
          )}
          <span className="gen-badge">第 {generation} 世代</span>
        </div>

        <nav className="tab-nav">
          <button
            className={`tab-btn ${activeTab === 'nurture' ? 'active' : ''}`}
            onClick={() => setActiveTab('nurture')}
          >
            <span>🐣</span> 育成画面
          </button>
          <button
            className={`tab-btn ${activeTab === 'dex' ? 'active' : ''}`}
            onClick={() => setActiveTab('dex')}
          >
            <span>📖</span> 図鑑
          </button>
        </nav>

        {onLogout && (
          <button
            onClick={onLogout}
            style={{
              background: 'rgba(255,255,255,0.08)',
              border: '1px solid rgba(255,255,255,0.15)',
              color: 'var(--text-muted)',
              padding: '0.4rem 0.8rem',
              borderRadius: '10px',
              fontSize: '0.8rem',
              cursor: 'pointer'
            }}
          >
            ログアウト
          </button>
        )}
      </div>
    </header>
  )
}

