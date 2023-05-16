function Aside({ title, categories }) {
  return (
    <div className="mt-[282px] w-[13rem] mr-[4.125rem]">
      <div className="w-[7.3rem] border-b-[3px] border-solid border-black3 font-bold text-[25px] text-black3 pb-[15px] mb-[2rem]">
        {title}
      </div>
      <div>
        {categories.map(text => {
          return (
            <div className="flex flex-col items-start py-[15px]">
              <button className="text-[17px] font-normal text-gray6 border-none">
                {text}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default Aside;
