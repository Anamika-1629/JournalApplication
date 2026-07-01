import Button from './Button'
import styles from './EmptyState.module.css'

export default function EmptyState({ title, description, actionLabel, onAction, icon }) {
  return (
    <div className={styles.wrapper}>
      <div className={styles.illustration}>
        {icon || (
          <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
            <rect x="8" y="4" width="40" height="52" rx="4" fill="#EEF2FF" stroke="#C7D2FE" strokeWidth="1.5"/>
            <line x1="16" y1="20" x2="40" y2="20" stroke="#A5B4FC" strokeWidth="2" strokeLinecap="round"/>
            <line x1="16" y1="28" x2="40" y2="28" stroke="#A5B4FC" strokeWidth="2" strokeLinecap="round"/>
            <line x1="16" y1="36" x2="32" y2="36" stroke="#A5B4FC" strokeWidth="2" strokeLinecap="round"/>
            <circle cx="48" cy="48" r="12" fill="#4F46E5"/>
            <line x1="48" y1="43" x2="48" y2="53" stroke="white" strokeWidth="2" strokeLinecap="round"/>
            <line x1="43" y1="48" x2="53" y2="48" stroke="white" strokeWidth="2" strokeLinecap="round"/>
          </svg>
        )}
      </div>
      <h3 className={styles.title}>{title}</h3>
      <p className={styles.description}>{description}</p>
      {actionLabel && onAction && (
        <Button onClick={onAction}>{actionLabel}</Button>
      )}
    </div>
  )
}
