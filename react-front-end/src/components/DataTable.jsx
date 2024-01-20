import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const DataTable = ({ headers, data, url, isEditModalOpen, setIsEditModalOpen, isNewUser, setIsNewUser }) => {
  const [selectedUser, setSelectedUser] = useState(null);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

  const navigate = useNavigate();
  const handleDelete = (url1) => {
    fetch(url1, {
      method: 'DELETE',
    }).then(() => {
      navigate('/users');
    });
  };
  const openCreateModal = () => {
    setIsCreateModalOpen(true);
  };
  const closeCreateModal = () => {
    setIsCreateModalOpen(false);
  };

  const closeEditModal = () => {
    setIsEditModalOpen(false);
    if (isNewUser) {
      setIsNewUser(false);
    }
  };
  const openEditModal = () => {
    setIsEditModalOpen(true);
  };

  const handleShow = async (url1) => {
    try {
      const response = await fetch(url1);
      const userData = await response.json();
      console.log('User Details:', userData);
      setSelectedUser(userData);
      openCreateModal();
    } catch (error) {
      console.error('Error fetching user details:', error);
    }
  };
  const handleEdit = async (url1) => {
    try {
      const response = await fetch(url1);
      const userData = await response.json();
      console.log('User Details:', userData);
      setSelectedUser(userData);
      openEditModal();
    } catch (error) {
      console.error('Error fetching user details:', error);
    }
  };
  const handleFormSubmit = async (event) => {
    event.preventDefault();

    // Your logic to update the user data on the server
    // For example, you can use fetch with the PUT method
    try {
      if (isNewUser) {
        // Your logic to create a new user on the server
        // For example, you can use fetch with the POST method
        await fetch('http://localhost:8081/USER-SERVICE/api/users', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(selectedUser),
        });
      } else {
        // Your logic to update an existing user on the server
        // For example, you can use fetch with the PUT method
        await fetch(`http://localhost:8081/USER-SERVICE/api/users/${selectedUser.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(selectedUser),
        });
      }
      closeEditModal();
      navigate('/users');
    } catch (error) {
      console.error('Error updating user details:', error);
    }
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setSelectedUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  return (
    <div>
      <table className="min-w-full divide-y divide-gray-200 dark:bg-gray-800 text-gray-300 dark:text-gray-100 border-collapse w-full">
        <thead>
          <tr className="">
            {headers.map((header) => (
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium uppercase"
                key={header}
              >
                {header}
              </th>
            ))}
            <th>Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-200">
          {data.map((row) => (
            <tr key={row.id}>
              {Object.keys(row).map((key) => (
                <React.Fragment key={key}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm">{row[key]}</div>
                  </td>

                </React.Fragment>
              ))}
              <td>
                <button type="button" onClick={() => handleDelete(`${url}/${row.id}`)}>Delete</button>
                <button type="button" onClick={() => handleShow(`${url}/${row.id}`)}>View</button>
                <button type="button" onClick={() => handleEdit(`${url}/${row.id}`)}>Edit</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {/* Modal */}
      {isCreateModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center">
          <div className="absolute p-4 w-1/2 rounded-3xl bg-gray-700">
            <h2 className="text-xl font-bold mb-4 text-white">User Information</h2>
            {selectedUser && (
              <div className="text-white">
                <p>ID: {selectedUser.id}</p>
                <p>Name: {selectedUser.name}</p>
                <p>Email: {selectedUser.email}</p>
              </div>
            )}
            <button type="button" className="ml-2 bg-gray-500 p-2 text-white rounded-md" onClick={closeCreateModal}>
              Close
            </button>
          </div>
        </div>
      )}
      {isEditModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center">
          <div className="absolute p-4 w-1/2 rounded-3xl bg-gray-700">
            <h2 className="text-xl font-bold mb-4 text-white">Edit User</h2>
            <form onSubmit={handleFormSubmit}>
              <div className="mb-4">
                <label htmlFor="name" className="block text-sm font-medium text-white">
                  Name:

                  <input
                    type="text"
                    id="name"
                    name="name"
                    value={selectedUser?.name || ''}
                    onChange={handleInputChange}
                    className="mt-1 p-2 w-full border rounded-md bg-gray-800 text-white"
                  />
                </label>
              </div>
              <div className="mb-4">
                <label htmlFor="email" className="block text-white text-sm font-medium">
                  Email:

                  <input
                    type="email"
                    id="email"
                    name="email"
                    value={selectedUser?.email || ''}
                    onChange={handleInputChange}
                    className="mt-1 p-2 w-full border rounded-md bg-gray-800 text-white"
                  />
                </label>
              </div>
              <div className="flex justify-end">
                <button type="submit" className="bg-blue-500 text-white p-2 rounded-md">
                  Save Changes
                </button>
                <button type="button" onClick={closeEditModal} className="ml-2 bg-gray-500 p-2 text-white rounded-md">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};

export default DataTable;
