import { X } from 'lucide-react';


export default function Modal({ isVisible, onClose, children, album }) {

  if(!isVisible) return null;

  const handleClick = (s) => {

    if(s.target.id ==='wrapper') onClose();
  }

  return (
    <div className="fixed z-10 inset-0 flex justify-center items-center transition-colors bg-black/50 blackdrop-blur-sm" id='wrapper' onClick={handleClick}>
        <div className='relative w-5/12'>
            <div className='bg-white rounded-3xl'>  
                <button onClick={() => onClose()} className='absolute top-2 right-2 p-1 rounded-full text-gray-400 bg-white hover:bg-gray-200 hover:text-gray-600'><X /></button>
          
                {children}
            </div>
        </div>
    </div>
  )
}