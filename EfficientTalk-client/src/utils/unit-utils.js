/**
 * @description 将文件大小转换为可读性更好的形式
 * @param size 文件大小
 * @returns {string}
 */
export const translateFileSize = (size) => {
    if (size < 1024) {
        return size + " B";
    }
    else if (size < 1024 * 1024) {
        return (size / 1024).toFixed(2) + " KB";
    }
    else if (size < 1024 * 1024 * 1024) {
        return (size / (1024 * 1024)).toFixed(2) + " MB";
    }
    else {
        return (size / (1024 * 1024 * 1024)).toFixed(2) + " GB";
    }
};