import { useSelector } from 'react-redux';

import Header from '../components/layout/Header';
import Carousel from '../components/Carousel';
import Footer from '../components/layout/Footer';
import Card from '../components/UI/Card';
import Item from '../components/Item';

const CaurouselConfig = {
  auto: true,
  infinite: true,
  carouselIntervalTime: 3000,
  transitionDelay: 500,
};

function Home() {
  const { infos } = useSelector(state => state.developInfos);

  return (
    <div className="w-screen flex flex-col items-center bg-gray1">
      <Header />
      <div className="max-w-limit pt-[160px]">
        <Carousel {...CaurouselConfig} />
        <div className="flex w-full justify-between">
          {infos.map(info => (
            <Card key={info.id} width="31.5%">
              <Item {...info} />
            </Card>
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default Home;
