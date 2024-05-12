import React, { useState } from 'react';

const DataTable = ({ headers, data, url, isCreateModalOpen, setIsCreateModalOpen, isEditModalOpen, setIsEditModalOpen, isNewUser, setIsNewUser, setIsChanged, nameItem }) => {
  const [selectedUser, setSelectedUser] = useState(null);
  const handleDelete = async (url1) => {
    try {
      const response = await fetch(url1, {
        method: 'DELETE',
      });
      if (response.ok) {
      // Handle successful deletion
        setIsChanged(true); // Redirect to success page
      } else {
      // Handle error response
        throw new Error('Deletion failed');
      }
    } catch (error) {
    // Handle network or other errors
    // console.error('Error during deletion:', error);
    // Display an error message to the user
    }
  };
  const openCreateModal = () => {
    setIsCreateModalOpen(true);
  };
  const cleanEditModel = () => {
    setSelectedUser(null);
  };
  const closeCreateModal = () => {
    setIsCreateModalOpen(false);
    cleanEditModel();
  };
  const closeEditModal = () => {
    cleanEditModel();
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
      setSelectedUser(userData);

      openCreateModal();
    } catch (error) {
      //
    }
  };
  const handleEdit = async (url1) => {
    try {
      const response = await fetch(url1);
      const userData = await response.json();
      setSelectedUser(userData);
      openEditModal();
    } catch (error) {
      // console.error('Error fetching user details:', error);
    }
  };
  const handleFormSubmit = async (event) => {
    event.preventDefault();
    try {
      if (isNewUser) {
        // Your logic to create a new user on the server
        // For example, you can use fetch with the POST method
        await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(selectedUser),
        });
      } else {
        // Your logic to update an existing user on the server
        // For example, you can use fetch with the PUT method
        await fetch(`${url}/${selectedUser.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(selectedUser),
        });
      }
      closeEditModal();
      setIsChanged(true);
    } catch (error) {
      // console.error('Error updating user details:', error);
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
                className="px-6 py-3 text-left uppercase"
                key={header}
              >
                {header}
              </th>
            ))}
            <th className="px-6 py-3 text-left uppercase">Actions</th>
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
            <h2 className="text-xl font-bold mb-4 text-white">{nameItem} Information</h2>
            {selectedUser && (
              <div className="text-white ">
                {
                  headers.map((header, index) => (
                    <p key={index}> <span className="capitalize"> {header}:</span> {header !== 'category' ? (
                      selectedUser[header]
                    ) : (
                      selectedUser[header].name
                    )}
                    </p>
                  ))
                }
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
            <h2 className='text-xl font-bold mb-4 text-white'>{isCreateModalOpen ? 'Create' : 'Edit' } {nameItem}</h2>
            <form onSubmit={handleFormSubmit}>
              {headers.map((header) => (
                <div className="mb-4" key={header}>
                  <label htmlFor={header} className="block text-sm font-medium text-white">
                    {header}:
                    <input
                      type="text"
                      id={header}
                      name={header}
                      value={header !== 'category' && selectedUser?.[header] ? (
                        selectedUser[header]
                      ) : (selectedUser ? (
                        selectedUser[header].name
                      ) : '') || ''}
                      onChange={handleInputChange}
                      className="mt-1 p-2 w-full border rounded-md bg-gray-800 text-white"
                    />
                  </label>
                </div>
              ))}
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
