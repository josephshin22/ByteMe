import React from "react";

const ScheduleCard = ({semester}) => {
    return (
        <div className="group">
          <div className="shadow cursor-pointer flex flex-col space-y-1 bg-white rounded-lg p-4 outline-0 outline-border transition-all duration-100 group-hover:outline-1 group-hover:shadow-lg group-hover:-translate-y-0.5">
              <h2 className="text-lg font-medium">{semester.replace("-", " ")}</h2>
              <p className="text-xs text-slate-400">Junior Year Spring</p>

              <div className="flex flex-col space-y-1 mt-3">
                  <p>Schedule #1</p>
                  <p>Schedule #2</p>
              </div>
          </div>
        </div>
    );
}

export default ScheduleCard;
