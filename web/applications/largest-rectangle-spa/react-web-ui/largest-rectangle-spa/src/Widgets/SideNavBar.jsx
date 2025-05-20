import { theme } from '../Theme';

export default function SideNavBar() {
  return (
    <div className="w-48 bg-secondary p-4" style={{ backgroundColor: theme.colors.secondary }}>
    <ul className="list-none p-0">
    {['Home', 'About', 'Contact'].map((item) => (
      <li key={item} className="mb-2">
      <a href="#" className="text-white hover:underline">{item}</a>
      </li>
    ))}
    </ul>
    </div>
  );
}
