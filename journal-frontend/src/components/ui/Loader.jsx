import styles from './Loader.module.css'

export default function Loader({ fullPage = false, size = 'md', text = '' }) {
  if (fullPage) {
    return (
      <div className={styles.fullPage}>
        <div className={[styles.spinner, styles[size]].join(' ')} />
        {text && <p className={styles.text}>{text}</p>}
      </div>
    )
  }

  return (
    <div className={styles.inline}>
      <div className={[styles.spinner, styles[size]].join(' ')} />
      {text && <p className={styles.text}>{text}</p>}
    </div>
  )
}
