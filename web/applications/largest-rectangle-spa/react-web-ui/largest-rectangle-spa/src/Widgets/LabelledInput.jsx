import { theme } from '../Theme';

export default function LabelledInput({ label, value, onChange }) {
  return (
    <div className="flex flex-col">
    <label className="mb-1 font-main text-text" style={{ color: theme.colors.text }}>{label}</label>
    <input
    type="number"
    value={value}
    onChange={(e) => onChange(e.target.value)}
    className="p-2 border border-primary rounded"
    style={{ borderColor: theme.colors.primary }}
    />
    </div>
  );
}
