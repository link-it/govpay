export interface IExport {

  exportData(data?: any);

  saveFile?(data: any, structure: any, ext: string);

  jsonToCsv?(name: string, jsonData: any);
}
