import React from "react";

const ScheduleCard = () => {
    return (
      <div className="shadow-md cursor-pointer flex flex-col space-y-1 bg-white rounded-lg p-4 outline-0 outline-slate-300 hover:outline-1 outline-offset-[-1px]">
          <h2 className="text-lg font-medium">Spring 2024</h2>
          <p className="text-xs text-slate-400">Junior Year Spring</p>

          <div className="flex flex-col space-y-1 mt-3">
              <p>Schedule #1</p>
              <p>Schedule #2</p>
          </div>
      </div>
    );
}

export default ScheduleCard;
