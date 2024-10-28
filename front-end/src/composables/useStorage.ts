import { ref } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { projectStorage } from '@/configs/firebase'
import { getBlob, getDownloadURL, getMetadata, ref as storageRef, uploadBytes } from 'firebase/storage'
import { v4 as uuidv4 } from 'uuid'

const useStorage = (name: string) => {
  const authStore = useAuthStore()
  const username = authStore.username
  const filePath = ref<string>('')
  const url = ref<string>('')
  const productUrl = ref<string>('')
  const error = ref<string>('')

  const uploadFile = async (file: File) => {
    filePath.value = `${name}/${username}/${file.name}`
    const fileRef = storageRef(projectStorage, filePath.value)
    try {
      await uploadBytes(fileRef, file)
      url.value = await getDownloadURL(fileRef)
      return url
    } catch (error: any) {
      console.log(error)
      error.value = error.message
    }
  }
  const uploadProductImage = async (file: File, id: string) => {
    const uuid = uuidv4()
    filePath.value = `${name}/${id}/${uuid}`
    const fileRef = storageRef(projectStorage, filePath.value)
    try {
      await uploadBytes(fileRef, file)
      productUrl.value = await getDownloadURL(fileRef)
      return productUrl
    } catch (error: any) {
      console.log(error)
      error.value = error.message
    }
  }

  const fetchImageMetadata = async (url: string) => {
    try {
      const fileRef = storageRef(projectStorage, url)
      const metadata = await getMetadata(fileRef)
      console.log(metadata.contentType)
      return metadata.contentType
    } catch (err) {
      console.error('Failed to fetch image metadata:', err)
      return null
    }
  }
  
  const uploadHtmlContent = async (htmlContent: string) => {
    const blob = new Blob([htmlContent], {type: 'text/html'});
    filePath.value = `${name}/${username}/description.html`;

    const fileRef = storageRef(projectStorage, filePath.value);
    try {
      await uploadBytes(fileRef, blob);
      url.value = await getDownloadURL(fileRef);
      return url;
    } catch (error: any) {
      console.log(error);
      error.value = error.message;
    }
  }

  const loadDescriptionFromUrl = async (filePath: string) => {
    try {
      const fileRef = storageRef(projectStorage, filePath)
      const blob = await getBlob(fileRef)
      const text = await blob.text()
      return text
    } catch (error) {
      console.error('Error loading description:', error)
      return null
    }
  }

  return { error, url, uploadFile, filePath, uploadProductImage, productUrl, fetchImageMetadata, uploadHtmlContent, loadDescriptionFromUrl }
}

export default useStorage
