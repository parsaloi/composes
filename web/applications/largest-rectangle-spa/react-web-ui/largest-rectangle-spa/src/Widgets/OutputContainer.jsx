export default function OutputContainer({ result }) {
  return (
    <div className="output-container">
      {result !== null
        ? `Largest rectangle area: ${result}`
        : 'No result yet'}
    </div>
  );
}
