import { Link } from "react-router-dom";

function NotFound() {

    return (
        <div className="flex flex-col items-center justify-center space-y-2">
            <h1 className="font-semibold text-xl mb-4">Page Not Found</h1>
            <Link to="/" className="underline" >Go home</Link>
        </div>

    );
}

export default NotFound;
