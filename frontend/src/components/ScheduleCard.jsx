import React, {useEffect, useState} from "react";
import { Button } from "@/components/ui/button.jsx";

import api from "@/api.js";

const ScheduleCard = ({semester}) => {

    const [schedules, setSchedules] = useState([]);

    useEffect(() => {
        api.get(`/schedules?semester=${semester}`)
            .then((response) => {
                setSchedules(response.data);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, [semester]);


    return (
        <div className="group ">
          <div className="shadow cursor-pointer flex flex-col space-y-1 bg-white rounded-lg p-4 outline-0 outline-border transition-all duration-100 group-hover:outline-1 group-hover:shadow-lg group-hover:-translate-y-0.5">
              <h2 className="text-lg font-medium">{semester.replace("_", " ")}</h2>
              <p className="text-xs text-slate-400">Junior Year {semester.split("_").pop()}</p>

              <div className="flex flex-col space-y-1 mt-3">
                  {schedules.length ? (
                      <>
                          {schedules.map((schedule, index) => (
                                  <p key={index}>{schedule.name}</p>
                          ))}
                      </>
                  ):(
                      <p className="text-slate-500">No schedules</p>
                  )}

              </div>
           
          </div>
        </div>
    );
}

export default ScheduleCard;
