import { theme } from '../Theme';

export default function TopNavBar({ title }) {
  return (
    <div className="bg-primary text-white p-4 w-full" style={{ backgroundColor: theme.colors.primary }}>
    <h1 className="m-0 font-header text-2xl">{title}</h1>
    </div>
  );
}
