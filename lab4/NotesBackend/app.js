// подключение express
const express = require("express");

var uuid = require('uuid');
var moment = require('moment');
const { Sequelize, Op, Model, DataTypes } = require("sequelize");


const sequelize = new Sequelize('NotesDB', 'postgres', '6458', {
    host: 'localhost',
    dialect:  'postgres' 
});
  


const Notes = sequelize.define('Notes', {
    id: {
        type: DataTypes.UUID,
        allowNull: false,
        defaultValue: DataTypes.UUIDV4,
        primaryKey: true
    },
    text: {
        type: DataTypes.STRING,
        allowNull: false,
        defaultValue: ""
    },
    title: {
        type: DataTypes.STRING,
        allowNull: false,
        defaultValue: ""
    }
    }, {
    
    });
    


// создаем объект приложения
const app = express();

// // определяем обработчик для маршрута "/"

// app.get("/", async function(request, response){
//     await sequelize.sync({ force: true });
//     const first = Notes.build({ title: "First", id: uuid.v4() });
//     await first.save();
//     response.send(first.title)
// });


app.get("/", async function(request, response){
    const notes = await Notes.findAll();
    response.json(notes);
});
  



app.get("/GetNoteById", async function(request, response){
    const noteId = request.query.id;
    const note = await Notes.findByPk(noteId);
    response.send(note)
});


app.post("/CreateNote", async function(request, response){
    const noteTitle = request.query.title;
    const noteText = request.query.text;
    const note = await Notes.create({ title: noteTitle, text: noteText, createdAt: moment().format("YYYY-MM-DD HH:mm:ss"), id: uuid.v4()});
    await note.save();
    response.send(note.id)
});

app.post("/EditNote", async function(request, response){
    const noteId = request.query.id;
    const noteTitle = request.query.title;
    const noteText = request.query.text;
    const note = await Notes.findByPk(noteId);
    note.text = noteText;
    note.title = noteTitle;
    note.updatedAt = moment().format("YYYY-MM-DD HH:mm:ss");
    await note.save();
    response.send(note.id);
});


// начинаем прослушивать подключения на 4000 порту
app.listen(4000);
