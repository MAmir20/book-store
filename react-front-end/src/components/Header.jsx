import React from 'react';

const Header = ({ category, title }) => (
  <div className=" mb-10 dark:bg-gray-800 text-gray-300 dark:text-gray-100 border-collapse w-full">
    <p className="text-lg text-white-400">{category}</p>
    <p className="text-3xl font-extrabold tracking-tight text-white-900">
      {title}
    </p>
  </div>
);

export default Header;
