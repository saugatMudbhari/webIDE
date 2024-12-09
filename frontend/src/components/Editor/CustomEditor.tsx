import React, {useState} from 'react';
import MonacoEditor from 'react-monaco-editor';


interface CustomEditorProps {
    language: string;
    code: string;
    onChange: (newCode: string) => void;
}

const CustomEditor: React.FC<CustomEditorProps> = ({language, code, onChange}) => {
    const handleEditorChange = (newValue: string) => {
        onChange(newValue);
    };
    return (
        <MonacoEditor
            height="95vh"
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
    );
};

export default CustomEditor;
