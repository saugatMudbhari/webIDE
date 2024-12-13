import React, { useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import '../ChooseLanguage.css'
import TerminalLayout from "../Terminal/TerminalLayout.tsx";

interface CustomEditorProps {
    language: string;
    code: string;
    onChange: (newCode: string) => void;
}

const CustomEditor: React.FC<CustomEditorProps> = ({ language, code, onChange }) => {
    const [paramValue, setParamValue] = useState<string[]>([]);
    const [currentInput, setCurrentInput] = useState<string>('');

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCurrentInput(event.target.value);
    };

    const addParameter = () => {
        if (currentInput.trim()) {
            setParamValue(prevValue => [...prevValue, currentInput.trim()]);
            setCurrentInput('');
        }
    };

    const removeParameter = (indexToRemove: number) => {
        setParamValue(prevValue =>
            prevValue.filter((_, index) => index !== indexToRemove)
        );
    };

    const handleEditorChange = (newValue: string) => {
        onChange(newValue);
    };

    return (
        <div className="code-editor-layout">
            <MonacoEditor
                height="70vh"
                width="75%"
                language={language}
                theme="vs-dark"
                value={code}
                onChange={handleEditorChange}
                options={{
                    fontSize: 16,
                    formatOnType: true,
                    autoClosingBrackets: true,
                    minimap: {enabled: true, scale: 1},
                    inlineSuggest: true,
                }}
            />

            <TerminalLayout code={code} language={language} parameters={paramValue} />
            <div
                style={{
                    width: "25%",
                    height: "70vh",
                    backgroundColor: "#282c34",
                    color: "#fff",
                    padding: "20px",
                    borderLeft: "1px solid #444",
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                <h3>Parameter Form</h3>

                <div style={{ display: 'flex', marginBottom: "10px" }}>
                    <input
                        type="text"
                        placeholder="Enter parameter"
                        value={currentInput}
                        onChange={handleInputChange}
                        style={{
                            flex: 1,
                            padding: "8px",
                            borderRadius: "4px 0 0 4px",
                            border: "1px solid #ccc",
                        }}
                    />
                    <button
                        onClick={addParameter}
                        style={{
                            padding: "8px",
                            borderRadius: "0 4px 4px 0",
                            backgroundColor: "#007bff",
                            color: "white",
                            border: "1px solid #007bff",
                        }}
                    >
                        Add
                    </button>
                </div>

                {/* Display added parameters */}
                <div style={{
                    overflowY: 'auto',
                    maxHeight: '300px',
                    backgroundColor: '#1e2227',
                    borderRadius: '4px',
                    padding: '10px',
                }}>
                    {paramValue.length > 0 ? (
                        paramValue.map((param, index) => (
                            <div
                                key={index}
                                style={{
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center',
                                    marginBottom: "5px",
                                    padding: "5px",
                                    backgroundColor: '#2c313a',
                                    borderRadius: '4px'
                                }}
                            >
                                <span>{param}</span>
                                <button
                                    onClick={() => removeParameter(index)}
                                    style={{
                                        backgroundColor: "#dc3545",
                                        color: "white",
                                        border: "none",
                                        borderRadius: "4px",
                                        padding: "4px 8px",
                                    }}
                                >
                                    Remove
                                </button>
                            </div>
                        ))
                    ) : (
                        <p style={{ color: '#6c757d', textAlign: 'center' }}>
                            No parameters added
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CustomEditor;
