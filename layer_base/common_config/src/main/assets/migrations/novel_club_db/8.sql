ALTER TABLE ReadTimeRecord RENAME TO ReadTimeRecord_Old;
create table ReadTimeRecord
(
	_id INTEGER PRIMARY KEY AUTOINCREMENT,
	uid TEXT,
	bid TEXT,
	startTime TEXT,
	duration INTEGER,
	content TEXT
);
insert into ReadTimeRecord(_id, uid, bid, startTime, duration, content) select _id, uid, bid, startTime, duration, content from ReadTimeRecord_Old;
drop table ReadTimeRecord_Old;

ALTER TABLE BookInfo RENAME TO BookInfo_Old;
create table BookInfo
(
	book_id TEXT PRIMARY KEY,
	user_id TEXT,
	copyright TEXT,
	created TEXT,
	description TEXT,
	recDesc TEXT,
	cover TEXT,
	status INTEGER,
	title TEXT,
	wordCount TEXT,
	latestChapterTip TEXT,
	lastChapter TEXT,
	isUpdate INTEGER,
	isAd INTEGER,
	sale_type INTEGER,
	adds_count INTEGER,
	chapter_count INTEGER,
	views_count INTEGER,
	rate INTEGER,
	rank INTEGER,
	category TEXT,
	score TEXT,
	author_id TEXT,
	author_name TEXT,
	category_id TEXT,
	category_name TEXT,
	create_time TEXT,
	update_time TEXT,
	intro TEXT,
	updated TEXT
);
insert into BookInfo(book_id, user_id, copyright, created, intro, recDesc, cover, status, title, wordCount, latestChapterTip, lastChapter, isUpdate, isAd, sale_type, adds_count, chapter_count, views_count, rank, category) select bid, uid, copyright, created, description, recDesc, poster, status, title, wordCount, latestChapterTip, lastChapter, isUpdate, isAd, free, adds_num, chapters_num, view_num, rank, category from BookInfo_Old;
drop table BookInfo_Old;
COMMIT;