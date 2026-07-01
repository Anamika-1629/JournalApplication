import { useToast } from '../../contexts/ToastContext'
import styles from './Toast.module.css'

function ToastItem({ toast, onRemove }) {
  return (
    <div className={[styles.toast, styles[toast.type]].join(' ')}>
      <span className={styles.icon}>
        {toast.type === 'success' && '✓'}
        {toast.type === 'error' && '✕'}
        {toast.type === 'warning' && '!'}
      </span>
      <span className={styles.message}>{toast.message}</span>
      <button className={styles.close} onClick={() => onRemove(toast.id)} aria-label="Dismiss">
        ×
      </button>
    </div>
  )
}

export default function ToastContainer() {
  const { toasts, removeToast } = useToast()

  return (
    <div className={styles.container} role="region" aria-label="Notifications">
      {toasts.map((t) => (
        <ToastItem key={t.id} toast={t} onRemove={removeToast} />
      ))}
    </div>
  )
}
