import React, { useState } from 'react';
import { Header } from '../components';
import useFetch from '../components/useFetch';
import DataTable from '../components/DataTable';

const Users = () => {
  const { data: users, isPending, error } = useFetch('http://localhost:8081/USER-SERVICE/api/users');
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isNewUser, setIsNewUser] = useState(false); // Track if it's a new user or an existing one
  const openModel = () => {
    setIsEditModalOpen(true);
    setIsNewUser(true);
  };

  let filtredUsers = [];
  if (users) {
    filtredUsers = users.map((user) => ({
      id: user.id,
      name: user.name,
      email: user.email,
    }));
  }
  return (
    <div className="m-2 md:m-10 mt-24 p-2 md:p-10 rounded-3xl text-white bg-gray-800 dark:text-gray-800">
      <Header category="Page" title="Users" />
      <div className="mb-4">
        <button type="button" onClick={() => openModel(true)} className="bg-green-500 text-white p-2 rounded-md">
          Create New User
        </button>
      </div>
      { error && <div>{ error }</div> }
      { isPending && <div>Loading...</div> }
      { users && (
        <DataTable headers={['ID', 'Name', 'Email']} data={filtredUsers} url="http://localhost:8081/USER-SERVICE/api/users" isEditModalOpen={isEditModalOpen} setIsEditModalOpen={setIsEditModalOpen} isNewUser={isNewUser} setIsNewUser={setIsNewUser} />
      )}
    </div>
  );
};
export default Users;
