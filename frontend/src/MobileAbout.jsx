import { Link } from 'react-router-dom'
import './MobileApp.css'

function MobileAbout() {
  return (
    <div className="mobile-app">
      <header className="mobile-header">
        <Link to="/mobile" className="mobile-brand">Morsor</Link>
        <Link to="/mobile/about" className="mobile-nav-link">About</Link>
      </header>
      <main className="mobile-main mobile-about-main">
        <article className="mobile-about-content">
          <h1>Morsor</h1>
          <p>A list of lists navigator</p>
          <h2>Why?</h2>
          <p>
            The REAL goal: Vibe-code the whole thing. This is my first experience vibe-coding.
          </p>
          <p>But, as for what the app DOES:</p>
          <p>
            I&apos;m a list-maker. I have a few dozen lists which I want to be able to easily browse, search
            and do some analysis on. That&apos;s what the app does.
          </p>
          <h2>What&apos;s the name?</h2>
          <p>This is a re-write of a previous app I built, called Moocho.me.</p>
          <p>I like Walruses. I speak Spanish. Morsa is Walrus in Spanish.</p>
          <p>Moocho and Morsa both start with M. I used Cursor for this. Morsa + Cursor = Morsor.</p>
          <p>I like Lord of the Rings. There is a distance of 1 between Mordor and Morsor (Levenshtein, or between keys on a keyboard).</p>
          <h2>Features</h2>
          <ul>
            <li>Search all troves (lists), or a subset.</li>
            <li>Find duplicate or unique items across troves.</li>
          </ul>
          <h2>Where do I get the data?</h2>
          <p>Scripts and manual slogging.</p>
        </article>
      </main>
      <footer className="mobile-footer">
        <Link to="/mobile" className="mobile-footer-link">Back to search</Link>
        <Link to="/" className="mobile-footer-link">Desktop site</Link>
      </footer>
    </div>
  )
}

export default MobileAbout
