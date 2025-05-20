import LabelledInput from './LabelledInput';
import { theme } from '../Theme';

export default function InputWidget({ matrix, onUpdate }) {
  const addRow = () => {
    const colLength = matrix[0]?.length || 0;
    onUpdate([...matrix, Array(colLength).fill(0)]);
  };

  const addColumn = () => {
    onUpdate(matrix.map(row => [...row, 0]));
  };

  return (
    <div className="mb-4">
      <h3 className="text-lg font-semibold text-text">Enter Matrix</h3>
      <div className="matrix-input">
        {matrix.map((row, rowIndex) => (
          <div key={rowIndex} className="matrix-row">
            {row.map((value, colIndex) => (
              <LabelledInput
                key={`${rowIndex}-${colIndex}`}
                label={`Row ${rowIndex + 1}, Col ${colIndex + 1}`}
                value={value}
                onChange={(newValue) => {
                  const newMatrix = matrix.map((r, ri) =>
                    ri === rowIndex
                      ? r.map((v, ci) => (ci === colIndex ? parseInt(newValue) || 0 : v))
                      : r
                  );
                  onUpdate(newMatrix);
                }}
              />
            ))}
          </div>
        ))}
      </div>
      <div className="mt-2 flex gap-2">
        <button
          onClick={addRow}
          className="bg-primary text-white px-4 py-2 rounded hover:bg-secondary"
        >
          Add Row
        </button>
        <button
          onClick={addColumn}
          className="bg-secondary text-white px-4 py-2 rounded hover:bg-primary"
        >
          Add Column
        </button>
      </div>
    </div>
  );
}
