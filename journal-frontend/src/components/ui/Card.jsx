import styles from './Card.module.css'

export default function Card({ children, className = '', padding = 'md', elevated = false, ...props }) {
  return (
    <div
      className={[styles.card, styles[padding], elevated ? styles.elevated : '', className].join(' ')}
      {...props}
    >
      {children}
    </div>
  )
}
