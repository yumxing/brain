// 清理 params 的函数
function cleanParams(params) {
    const cleanedParams = {};
    for (const key in params) {
        if (params.hasOwnProperty(key)) {
            const value = params[key];
            if (value !== undefined && value !== null && value !== '') {
                cleanedParams[key] = value;
            }
        }
    }
    return cleanedParams;
}