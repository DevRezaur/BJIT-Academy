import React from "react";
import "./add-batch.scss";
import BatchInfo from "../../../components/batch-info/batch-info";

const AddBatch = () => {
  return (
    <>
      <div className="add-batch">
        <h3>Add New Training Batch</h3>

        <BatchInfo />
      </div>
    </>
  );
};

export default AddBatch;
