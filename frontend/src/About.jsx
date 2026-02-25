import { Link } from 'react-router-dom'
import './App.css'

function About() {
  return (
    <>
      <div className="about-page">
        <p className="about-placeholder">To Do!</p>
      </div>
      <hr className="backend-status-divider" />
      <footer className="app-footer">
        <Link to="/about" className="app-footer-link">About</Link>
      </footer>
    </>
  )
}

export default About
