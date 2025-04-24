import React, {useEffect, useState} from "react";
import api from "@/api.js";
import {Button} from "@/components/ui/button.jsx";

const ScheduleCard = ({semester}) => {

    const [schedules, setSchedules] = useState([]);
    const [showDeleteOptions, setShowDeleteOptions] = useState(false);

    useEffect(() => {
        api.get(`/schedules?semester=${semester}`)
            .then((response) => {
                setSchedules(response.data);
            })
            .catch((error) => {
                console.error("Error fetching schedules:", error);
            });
    }, [semester]);

    const handleDelete = (scheduleId) => {
        api.delete(`/schedules/${scheduleId}`)
            .then(() => {
                setSchedules((prev) => prev.filter(s => s.scheduleID !== scheduleId));
                setShowDeleteOptions(false);
            })
            .catch(err => {
                console.error("Error deleting schedule:", err);
            });
    };

    return (
        <div className="group ">
          <div className="shadow cursor-pointer flex flex-col space-y-1 bg-white rounded-lg p-4 outline-0 outline-border transition-all duration-100 group-hover:outline-1 group-hover:shadow-lg group-hover:-translate-y-0.5">
              <h2 className="text-lg font-medium">{semester.replaceAll("_", " ")}</h2>
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

              {!showDeleteOptions && schedules.length > 0 && (
                  <Button
                      onClick={() => setShowDeleteOptions(true)}
                      className="mt-2 w-28 bg-red-100 hover:bg-red-200 text-red-800 transition-colors duration-150"
                      size="sm"
                  >
                      Remove
                  </Button>
              )}

              {showDeleteOptions && schedules.length > 0 && (
                  <div className="absolute top-0 left-0 w-full h-full bg-white/90 p-4 flex flex-col gap-2 rounded-lg shadow-md">
                      <p className="font-medium">Select schedule to remove:</p>
                      {schedules.map((s) => (
                          <div key={s.scheduleID} className="w-28">
                              <Button
                                  onClick={() => handleDelete(s.scheduleID)}
                                  className="h-full w-full bg-red-100 hover:bg-red-200 text-red-800 transition-colors duration-150"
                                  size="sm"
                              >
                                  {s.name}
                              </Button>
                          </div>
                      ))}
                      <Button
                          variant="ghost"
                          onClick={() => setShowDeleteOptions(false)}
                          size="sm"
                      >
                          Cancel
                      </Button>
                  </div>
              )}
          </div>
        </div>
    );
}

export default ScheduleCard;
