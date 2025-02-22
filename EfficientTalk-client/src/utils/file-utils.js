// 支持的文件集合
const fileTypeSet = [
    "pdf",
    "xls",
    "xlsx",
    "excel",
    "doc",
    "docx",
    "ppt",
    "pptx",
    "word",
    "wps",
    "zip",
    "png",
    "jpg",
    "jpeg",
    "txt",
    "html",
    "htm",
    "mp3",
    "mp4",
    "image",
    "mpeg",
    "java",
    "xml",
    "gif",
    "flac",
    "flv",
    "avi",
    "bmp",
    "aac",
    "ape",
    "asf",
    "asp",
    "ceb",
    "cebx",
    "divx",
    "dot",
    "dps",
    "dpt",
    "dwg",
    "et",
    "ett",
    "gd",
    "giff",
    "jasper",
    "midi",
    "mkv",
    "mov",
    "ofd",
    "ogg",
    "pcm",
    "ps",
    "rm",
    "rtf",
    "svg",
    "sep",
    "tif",
    "tiff",
    "wav",
    "wmv",
    "wpt",
    "wt",
    "xsd",
];

/**
 * 获取文件图标
 * @param type 文件类型
 */
export const getFileIcon = (type) => {
    const fileType = type.toLowerCase();
    for (let i = 0; i < fileTypeSet.length; i++) {
        if (fileTypeSet[i] === fileType) {
            return "/public/file-icon/" + fileTypeSet[i] + ".png";
        }
    }
    return "/public/file-icon/other.png";
};