import React, {useState} from "react";

interface TerminalLayoutProps {
    language: string;
    code: string;
    parameters: string[];
}

const TerminalLayout: React.FC<TerminalLayoutProps> = ({ language, code, parameters }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [result, setResult] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const executeCode = async () => {
        setIsLoading(true);
        setError(null);
        setResult(null);

        try {
            const response = await fetch('http://localhost:8080/execute-language', {
                method: 'POST',
                body: JSON.stringify({
                    language: language,
                    code: code,
                    parameters: parameters,
                }),
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            setResult(data.result);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An unknown error occurred');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <button onClick={executeCode} disabled={isLoading}>
                {isLoading ? 'Executing...' : 'Execute Code'}
            </button>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            {result && <div>Result: {result}</div>}
        </div>
    );
};

export default TerminalLayout;
