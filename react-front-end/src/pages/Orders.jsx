import React, { useEffect, useState } from 'react';
import { Header } from '../components';
import useFetch from '../components/useFetch';
import DataTable from '../components/DataTable';

// const Orders = () => {
//   const editing = { allowDeleting: true, allowEditing: true };
//   return (
//     <div className="m-2 md:m-10 mt-24 p-2 md:p-10 bg-white rounded-3xl">
//       <Header category="Page" title="Orders" />
//       <GridComponent
//         id="gridcomp"
//         dataSource={ordersData}
//         allowPaging
//         allowSorting
//         allowExcelExport
//         allowPdfExport
//         contextMenuItems={contextMenuItems}
//         editSettings={editing}
//       >
//         <ColumnsDirective>
//           {/* eslint-disable-next-line react/jsx-props-no-spreading */}
//           {ordersGrid.map((item, index) => <ColumnDirective key={index} {...item} />)}
//         </ColumnsDirective>
//         <Inject services={[Resize, Sort, ContextMenu, Filter, Page, ExcelExport, Edit, PdfExport]} />
//       </GridComponent>
//     </div>
//   );
// };
// export default Orders;
const Orders = () => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isNewBook, setIsNewBook] = useState(false); // Track if it's a new book or an existing one
  const { data, isPending, error } = useFetch('http://localhost:8087/api/orders');
  const [books, setBooks] = useState([]);
  const [isChanged, setIsChanged] = useState(false); // Track if the data has changed
  const openModel = () => {
    setIsEditModalOpen(true);
    setIsNewBook(true);
  };
  useEffect(() => {
    setIsChanged(false);
    const fetchOrders = async () => {
      const response = await fetch('http://localhost:8087/api/orders');
      const bookData = await response.json();
      setBooks(bookData);
    };
    fetchOrders();
  }, [isChanged]);
  let filtredBooks = [];
  if (books) {
    filtredBooks = books.map((order) => ({
      id: order.id,
      user: 'Ali Tounsi',
      status: order.status,
      payment: order.payment,
      date: (new Date(order.date)).toLocaleDateString('en-FR'),
    }));
  }
  let headers = [];
  if (filtredBooks.length > 0) {
    headers = Object.keys(filtredBooks[0]);
  }

  return (
    <div className="m-2 md:m-10 mt-24 p-2 md:p-10 rounded-3xl text-white bg-gray-800 dark:text-gray-800">
      <Header category="Page" title="Orders" />
      <div className="mb-4">
        <button type="button" onClick={() => openModel(true)} className="bg-green-500 text-white p-2 rounded-md">
          Create New Order
        </button>
      </div>
      { error && <div>{ error }</div> }
      { isPending && <div>Loading...</div> }
      { books && (
        <DataTable headers={headers} nameItem="Order" data={filtredBooks} url="http://localhost:8087/api/orders" isEditModalOpen={isEditModalOpen} setIsEditModalOpen={setIsEditModalOpen} isNewUser={isNewBook} setIsNewUser={setIsNewBook} setIsChanged={setIsChanged} />
      )}
    </div>
  );
};
export default Orders;
