import React, { useEffect, useState } from 'react';
import { Header } from '../components';
import useFetch from '../components/useFetch';
import DataTable from '../components/DataTable';

const Users = () => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isNewUser, setIsNewUser] = useState(false); // Track if it's a new user or an existing one
  const {data, isPending, error } = useFetch('http://localhost:8087/api/users');
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [users, setUsers] = useState([]);
  const [isChanged, setIsChanged] = useState(false); // Track if the data has changed
  const openModel = () => {
    setIsCreateModalOpen(true);
    setIsEditModalOpen(false);
    setIsNewUser(true);
  };
  useEffect(() => {
    setIsChanged(false);
    const fetchUsers = async () => {
      const response = await fetch('http://localhost:8087/api/users');
      const userData = await response.json();
      setUsers(userData);
    };
    fetchUsers();
  }, [isChanged]);
  let filtredUsers = [];
  if (users) {
    filtredUsers = users.map((user) => ({
      id: user.id,
      name: user.name,
      email: user.email,
    }));
  }
  let headers = [];
  if (filtredUsers.length > 0) {
    headers = Object.keys(filtredUsers[0]);
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
        <DataTable headers={headers} data={filtredUsers} nameItem="User" url="http://localhost:8087/api/users" isEditModalOpen={isEditModalOpen} setIsEditModalOpen={setIsEditModalOpen} isEditModalOpen={isCreateModalOpen} setIsEditModalOpen={setIsCreateModalOpen}  isNewUser={isNewUser} setIsNewUser={setIsNewUser} setIsChanged={setIsChanged} />
      )}
    </div>
  );
};
export default Users;
