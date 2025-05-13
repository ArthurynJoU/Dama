import { BrowserRouter, Routes, Route } from 'react-router-dom';
import GamePage from './pages/GamePage';
import HighScoresPage from './pages/HighScoresPage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<GamePage />} />
                <Route path="/scores" element={<HighScoresPage />} />
            </Routes>
        </BrowserRouter>
    );
}
export default App;
