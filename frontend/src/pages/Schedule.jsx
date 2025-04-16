import { useParams } from "react-router-dom";

function Schedule() {
    const { semester } = useParams();

    return (
        <div>
            <h1>Schedule for {semester.replace("-", " ")}</h1>
            <p>Here are the details for {semester}.</p>
        </div>
    );

}

export default Schedule;
