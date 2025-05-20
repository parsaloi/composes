import { theme } from '../Theme';

export default function IconButton({ onClick, label }) {
  return (
    <button
    onClick={onClick}
    className="icon-button bg-primary text-white"
    style={{ backgroundColor: theme.colors.primary }}
    >
    {label}
    </button>
  );
}
