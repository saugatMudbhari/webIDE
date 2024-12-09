import React, {Component, useState} from "react";
import '../ChooseLanguage.css'

interface ChooseLanguageProps {
    languages: string[];
    onSelectLanguage: (language: string) => void;
    defaultLanguage?: string;
}

const ChooseLanguage: React.FC<ChooseLanguageProps> = ({ languages, onSelectLanguage, defaultLanguage = "" }) => {
    const [selectedLanguage, setSelectedLanguage] = useState<string>(defaultLanguage);

    const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const language = event.target.value;
        setSelectedLanguage(language);
        onSelectLanguage(language);
    };
    return (
        <div className="language-selector">
            <label htmlFor="language-select" className="language-label">
                Choose a Language
            </label>
            <select
                id="language-select"
                className="language-dropdown"
                value={selectedLanguage}
                onChange={handleChange}
            >
                {languages.map((language, index) => (
                    <option key={index} value={language}>
                        {language}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default ChooseLanguage;