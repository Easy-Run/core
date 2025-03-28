"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.initialize = initialize;
// environment.ts
const javaBridge_1 = __importDefault(require("./javaBridge"));
function initialize() {
    return new Promise((resolve, reject) => {
        (0, javaBridge_1.default)(['initialize'], (err, stdout, stderr) => {
            if (err) {
                reject(err);
            }
            else {
                resolve();
            }
        });
    });
}
