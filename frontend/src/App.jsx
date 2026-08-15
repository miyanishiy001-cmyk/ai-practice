import React, { useState, useEffect } from 'react'
import Header from './components/Header'
import NurtureView from './components/NurtureView'
import DexView from './components/DexView'
import LoginView from './components/LoginView'
import './App.css'

function App() {
  const [user, setUser] = useState(null)
  const [activeTab, setActiveTab] = useState('nurture')
  const [currentCharacter, setCurrentCharacter] = useState(null)

  // 初期起動時に localStorage からログイン情報をロード
  useEffect(() => {
    const savedUser = localStorage.getItem('ai_game_user')
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser))
      } catch (e) {
        localStorage.removeItem('ai_game_user')
      }
    }
  }, [])

  const handleLoginSuccess = (userData) => {
    setUser(userData)
    localStorage.setItem('ai_game_user', JSON.stringify(userData))
  }

  const handleLogout = () => {
    setUser(null)
    setCurrentCharacter(null)
    localStorage.removeItem('ai_game_user')
  }

  if (!user) {
    return (
      <div className="app-container">
        <LoginView onLoginSuccess={handleLoginSuccess} />
      </div>
    )
  }

  return (
    <div className="app-container">
      <Header
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        generation={currentCharacter?.generation || 1}
        user={user}
        onLogout={handleLogout}
      />

      <main style={{ marginTop: '0.5rem' }}>
        {activeTab === 'nurture' && (
          <NurtureView
            user={user}
            onCharacterUpdate={(char) => setCurrentCharacter(char)}
          />
        )}
        {activeTab === 'dex' && (
          <DexView user={user} />
        )}
      </main>
    </div>
  )
}

export default App
