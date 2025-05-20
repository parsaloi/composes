import { theme } from '../Theme';

export default function MessageDialog({ message, onClose }) {
  return (
    <div className="message-dialog-overlay">
    <div className="message-dialog" style={{ backgroundColor: 'white' }}>
    <div className="text-text mb-4" style={{ color: theme.colors.text }}>{message}</div>
    <button
    onClick={onClose}
    className="px-4 py-2 bg-error text-white rounded hover:bg-red-700"
    style={{ backgroundColor: theme.colors.error, color: 'white' }}
    >
    Close
    </button>
    </div>
    </div>
  );
}
