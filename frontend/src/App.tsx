import CustomEditor from "./components/Editor/CustomEditor.tsx";
import ChooseLanguage from "./components/DropDown/ChooseLanguage.tsx";
import {useState} from "react";
import languageSnippets from "./components/Editor/DefaultLanguageSnippet.ts";

function App() {
    const languages: string[] = ["javascript", "c", "c++", "java", "python", "php"]
    const [selectedLanguage, setSelectedLanguage] = useState<string>(languages[0]);
    const [code, setCode] = useState<string>(languageSnippets[languages[0]]);

    const handleLanguageSelect = (language: string) => {
        setSelectedLanguage(language);
        // Automatically update code when language changes
        setCode(languageSnippets[language]);
    };

    const handleEditorChange = (newCode: string) => {
        setCode(newCode);  // Update the code state
    };


    return (
        <>
            <>
                <ChooseLanguage languages={languages} onSelectLanguage={handleLanguageSelect}
                                defaultLanguage={selectedLanguage}/>
                <CustomEditor language={selectedLanguage} code={code} onChange={handleEditorChange}/>
            </>
        </>
    )
}

export default App
