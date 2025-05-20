import { useState } from 'react';
import { from, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { GlobalStyles } from './Styles';
import { theme } from './Theme';
import TopNavBar from './Widgets/TopNavBar';
import SideNavBar from './Widgets/SideNavBar';
import Footer from './Widgets/Footer';
import InputWidget from './Widgets/InputWidget';
import IconButton from './Widgets/IconButton';
import OutputContainer from './Widgets/OutputContainer';
import MessageDialog from './Widgets/MessageDialog';

export default function App() {
  const [matrix, setMatrix] = useState([[0]]);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const validateMatrix = (matrix) => {
    if (!matrix.length) return 'Matrix cannot be empty';
    const firstRowLength = matrix[0].length;
    if (!matrix.every(row => row.length === firstRowLength)) return 'All rows must have the same length';
    if (!matrix.every(row => row.every(n => n === 0 || n === 1))) return "Matrix should contain only 0's and 1's";
    return null;
  };

  const calculateLargestRectangle = () => {
    const errorMsg = validateMatrix(matrix);
    if (errorMsg) {
      setError(errorMsg);
      return;
    }

    const calculate$ = from(
      fetch('http://localhost:8080/largest-rectangle', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(matrix),
      })
    ).pipe(
      switchMap(response => from(response.json())),
        map(data => data.largestRectangleArea),
           catchError(() => of({ error: 'Awaiting calculation service to be ready. Please try again later.' }))
    );

    calculate$.subscribe({
      next: (value) => {
        if (value.error) {
          setError(value.error);
        } else {
          setResult(value);
          setError(null);
        }
      },
      error: () => setError('An error occurred during calculation.'),
    });
  };

  return (
    <>
    <GlobalStyles />
    <div className="layout min-w-full" style={{ backgroundColor: theme.colors.background, color: theme.colors.text }}>
    <TopNavBar title="Largest Rectangle Calculator (1-based)" />
    <div className="main-content">
    <SideNavBar />
    <div className="content">
    <InputWidget matrix={matrix} onUpdate={setMatrix} />
    <IconButton
    onClick={calculateLargestRectangle}
    label="Calculate"
    />
    <OutputContainer result={result} />
    {error && (
      <MessageDialog
      message={error}
      onClose={() => setError(null)}
      />
    )}
    </div>
    </div>
    <Footer />
    </div>
    </>
  );
}
