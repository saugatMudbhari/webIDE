import React, { useState } from 'react';
import MonacoEditor from 'react-monaco-editor';

const CustomEditor: React.FC = () => {
    const [code, setCode] = useState<string>("var message = 'Monaco Editor!' \nconsole.log(message);");

    const handleEditorChange = (newValue: string) => {
        setCode(newValue);
    };

    return (
        <MonacoEditor
            height="400px"
            language="javascript"
            theme="vs-dark"
            value={code}
            onChange={handleEditorChange}
            options={{
                fontSize: 16,
                formatOnType: true,
                autoClosingBrackets: true,
                minimap: { enabled: true, scale: 1 },
                inlineSuggest: true,
            }}
        />
    );
};

export default CustomEditor;
