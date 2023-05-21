import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { Link } from 'react-router-dom';
import Aside from '../components/layout/Aside';
import Card from '../components/UI/Card';
import Item from '../components/Item';
import { fetchAllDevelopmentsAction } from '../store/developmentSlice';
import { ChevronDownIcon, PencilIcon } from '../components/Icons';
import Button from '../components/UI/Button';
import Accordian from '../components/UI/Accordian';

function AllDevelopments() {
  const [sortaActive, setSortaActive] = useState('전체');
  const [accordianVisible, setAccordianVisible] = useState(false);
  const [accordianFilterValue, setAccordianFilterValue] = useState('최신순');
  const { allDevelopments } = useSelector(state => state.developments);
  const dispatch = useDispatch();

  const onSortaButtonClickHandler = e => setSortaActive(prev => e.target.value);

  const onAccordianFilterVisibleClickHandler = () => setAccordianVisible(prev => !prev);
  const onAccordianFilterValueClickHandler = filterValue =>
    setAccordianFilterValue(prev => filterValue);
  // const onAcordian
  const activeStyle = 'bg-black3 text-white1';
  const notActiveStyle = 'bg-white1 text-black3';

  useEffect(() => {
    dispatch(fetchAllDevelopmentsAction());
  }, []);

  return (
    <>
      <section>
        <Aside
          title="Tech Stack"
          categories={{
            titles: [
              'javascript',
              'typescript',
              'Authentication',
              'Webpack',
              'Css-in-js',
              'React',
              'Vue',
              'Angular',
              'Nextjs',
              'Database',
              'Network',
              'Algorithm',
            ],
          }}
        />
      </section>
      <section className="pt-main-top w-full">
        <div className="relative flex justify-between mt-3">
          <div className="flex gap-2">
            {['전체', '글', '영상', '트렌드'].map((filterValue, index) => (
              <Button
                // eslint-disable-next-line react/no-array-index-key
                key={index}
                value={filterValue}
                onClick={e => onSortaButtonClickHandler(e)}
                className={`text-xs px-3 py-1 border-[1px] border-solid border-gray8 rounded-2xl ${
                  sortaActive === filterValue ? activeStyle : notActiveStyle
                }`}
              >
                {filterValue}
              </Button>
            ))}
          </div>
          <div className="absolute top-0 right-0 flex flex-col border-[1px] border-gray6 border-solid text-[0.625rem] cursor-pointer">
            <Button onClick={onAccordianFilterVisibleClickHandler}>
              <span className="p-2">최신순</span> <ChevronDownIcon className="mr-2" />
            </Button>
            {accordianVisible && (
              <Accordian
                onClickHandler={onAccordianFilterValueClickHandler}
                activeValue={accordianFilterValue}
                itemList={['최신순', '인기순']}
                className="border-t-[1px] p-2 border-gray6 border-solid"
                activeColor="text-activeBlue"
                visible={accordianVisible}
              />
            )}
          </div>
        </div>
        <div className="w-full flex flex-col max-w-limit">
          <h3 className="flex items-center text-[1.2rem] mt-5 border-b-[1px] border-solid border-gray4">
            <Link to="/developments/new" className="flex w-32 py-7 pr-4">
              <PencilIcon className="w-5 h-5 mr-3" />{' '}
              <span className="font-bold">글 쓰기</span>
            </Link>
          </h3>
          <div className="w-full flex justify-between flex-wrap gap-6 py-8">
            {allDevelopments.data.map(info => (
              <Card key={info.postId} flexItemwidth="30%">
                <Item {...info} />
              </Card>
            ))}
          </div>
        </div>
      </section>
    </>
  );
}

export default AllDevelopments;
