import { initializeApp } from 'firebase/app';
import { getStorage } from 'firebase/storage';


const firebaseConfig = {
  apiKey: 'AIzaSyCUJ5_oKqsCZsoEy3SYWYjG_jaEHb5W0No',
  authDomain: 'tuna03-test.firebaseapp.com',
  projectId: 'tuna03-test',
  storageBucket: 'tuna03-test.appspot.com',
  messagingSenderId: '488108802956',
  appId: '1:488108802956:web:7b16c949efdf0e0969cca9',
  measurementId: 'G-FPBR7Q09WM'
}

const app = initializeApp(firebaseConfig);

const projectStorage = getStorage(app);

export {projectStorage}