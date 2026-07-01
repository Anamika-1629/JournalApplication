import styles from './Input.module.css'

export default function Input({
  label,
  error,
  hint,
  id,
  className = '',
  textarea = false,
  rows = 6,
  ...props
}) {
  const inputId = id || label?.toLowerCase().replace(/\s+/g, '-')

  return (
    <div className={[styles.field, className].join(' ')}>
      {label && (
        <label htmlFor={inputId} className={styles.label}>
          {label}
        </label>
      )}
      {textarea ? (
        <textarea
          id={inputId}
          className={[styles.input, styles.textarea, error ? styles.hasError : ''].join(' ')}
          rows={rows}
          {...props}
        />
      ) : (
        <input
          id={inputId}
          className={[styles.input, error ? styles.hasError : ''].join(' ')}
          {...props}
        />
      )}
      {error && <span className={styles.error}>{error}</span>}
      {hint && !error && <span className={styles.hint}>{hint}</span>}
    </div>
  )
}
