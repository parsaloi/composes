import { theme } from '../Theme';

export default function OutputContainer({ result }) {
  return (
    <div className="output-container" style={{ backgroundColor: 'white', color: theme.colors.text }}>
    {result !== null
      ? `Largest rectangle area: ${result}`
      : 'No result yet'}
      </div>
  );
}
