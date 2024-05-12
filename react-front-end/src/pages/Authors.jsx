import React, { useEffect, useState } from 'react';
import { Header } from '../components';
import useFetch from '../components/useFetch';
import DataTable from '../components/DataTable';
import e from 'cors';

const Authors = () => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isNewBook, setIsNewBook] = useState(false); // Track if it's a new book or an existing one
  const { data, isPending, error } = useFetch('http://localhost:8087/api/authors');
  const [books, setBooks] = useState([]);
  const [isChanged, setIsChanged] = useState(false); // Track if the data has changed
  const openModel = () => {
    setIsEditModalOpen(true);
    setIsNewBook(true);
  };
  useEffect(() => {
    setIsChanged(false);
    const fetchBooks = async () => {
      const response = await fetch('http://localhost:8087/api/authors');
      const bookData = await response.json();
      setBooks(bookData);
    };
    fetchBooks();
  }, [isChanged]);
  let filtredBooks = [];
  if (books) {
    filtredBooks = books.map((book) => ({
      id: book.id,
      name: book.name,
      email: book.email,
    }));
  }
  let headers = [];
  if (filtredBooks.length > 0) {
    headers = Object.keys(filtredBooks[0]);
  }

  return (
    <div className="m-2 md:m-10 mt-24 p-2 md:p-10 rounded-3xl text-white bg-gray-800 dark:text-gray-800">
      <Header category="Page" title="Authors" />
      <div className="mb-4">
        <button type="button" onClick={() => openModel(true)} className="bg-green-500 text-white p-2 rounded-md">
          Create New Author
        </button>
      </div>
      { error && <div>{ error }</div> }
      { isPending && <div>Loading...</div> }
      { books && (
        <DataTable headers={headers} nameItem="Authors" data={filtredBooks} url="http://localhost:8087/api/authors" isEditModalOpen={isEditModalOpen} setIsEditModalOpen={setIsEditModalOpen} isNewUser={isNewBook} setIsNewUser={setIsNewBook} setIsChanged={setIsChanged} />
      )}
    </div>
  );
};
export default Authors;
