import { theme } from '../Theme';

export default function Footer() {
  return (
    <div className="bg-primary text-white p-4 text-center w-full" style={{ backgroundColor: theme.colors.primary }}>
    © 2025 Elvis Parsaloi |{' '}
    <a href="https://github.com/parsaloi/composes" className="text-white hover:underline">
    Source Code
    </a>
    </div>
  );
}
