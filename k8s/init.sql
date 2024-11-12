USE debook_db;
-- Drop tables in reverse order of dependency
DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `redis_token_blacklist`;
DROP TABLE IF EXISTS `restriction_history`;
DROP TABLE IF EXISTS `book_recommend`;
DROP TABLE IF EXISTS `chatroom_user_info`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `chatroom`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `token`;
DROP TABLE IF EXISTS `user`;

-- Create tables in dependency order
CREATE TABLE IF NOT EXISTS `user` (
	`user_id` BIGINT NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(300) NULL,
	`name` VARCHAR(255) NULL,
	`nickname` VARCHAR(255) NULL,
	`status` VARCHAR(30) NULL,
	`create_datetime` DATETIME NULL,
	`auth_provider` VARCHAR(255) NULL,
	`profile_img` VARCHAR(500) NULL,
	`withdraw_datetime` DATETIME NULL,
	`login_id` BIGINT NOT NULL,
	PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `token` (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` bigint(20) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `access_token_expire_datetime` bigint(20) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `refresh_token_expire_datetime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 debook_db.token:~0 rows (대략적) 내보내기
DELETE FROM `token`;

CREATE TABLE `book` (
	`book_id` BIGINT NULL AUTO_INCREMENT,
	`title` VARCHAR(70) NULL,
	`author` VARCHAR(110) NULL,
	`publisher` VARCHAR(10) NULL,
	`pubdate` VARCHAR(10) NULL,
	`info` VARCHAR(210) NULL,
	`img` VARCHAR(70) NULL,
	PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `chatroom` (
	`chatroom_id` BIGINT NOT NULL AUTO_INCREMENT,
	`book_id` BIGINT NOT NULL,
	`open_datetime` DATETIME NULL,
	`max_member_count` BIGINT NULL,
	`title` VARCHAR(30) NULL,
	`status` VARCHAR(10) NULL,
	PRIMARY KEY (`chatroom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `chatroom_user_info` (
	`chatroom_info_id` BIGINT NOT NULL AUTO_INCREMENT,
	`chatroom_id` BIGINT NOT NULL,
	`user_id` BIGINT NOT NULL,
	`member_join_datetime` DATETIME NULL,
	`chatroomUser_info_status` VARCHAR(255) NULL,
	PRIMARY KEY (`chatroom_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `review` (
	`review_id` BIGINT NOT NULL AUTO_INCREMENT,
	`book_id` BIGINT NOT NULL,
	`user_id` BIGINT NOT NULL,
	`create_datetime` DATETIME NULL,
	`update_datetime` DATETIME NULL,
	`delete_datetime` DATETIME NULL,
	`title` VARCHAR(255) NULL,
	`content` TEXT NULL,
	`rating` TINYINT NULL,
	`status` VARCHAR(10) NULL,
	PRIMARY KEY (`review_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `report` (
  `report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reporter_user_id` bigint(20) DEFAULT NULL,
  `reported_user_id` bigint(20) DEFAULT NULL,
  `review_id` bigint(20) DEFAULT NULL,
  `chatroom_id` bigint(20) DEFAULT NULL,
  `chat_id` bigint(20) DEFAULT NULL,
  `create_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `book_recommend` (
	`recommend_id` BIGINT NOT NULL AUTO_INCREMENT,
	`question_content` TEXT NULL,
	`response_content` TEXT NULL,
	`question_datetime` DATETIME NULL,
	`user_id` BIGINT NULL,
	PRIMARY KEY (`recommend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `restriction_history` (
	`restriction_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL,
   `restriction_status` varchar(20) NOT NULL,
	`create_datetime` DATETIME NULL,
	`end_datetime` DATETIME NULL,
	PRIMARY KEY (`restriction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `redis_token_blacklist` (
	`Key` VARCHAR(255) NOT NULL,
	`Field` VARCHAR(255) NULL,
	`Field2` VARCHAR(255) NULL,
	`Field3` VARCHAR(255) NULL,
	`Field4` VARCHAR(255) NULL,
	PRIMARY KEY (`Key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Add foreign keys after table creation
ALTER TABLE `chatroom` 
	ADD CONSTRAINT `FK_Book_TO_ChatRoom_1` 
	FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`);

ALTER TABLE `report` 
	ADD CONSTRAINT `FK_review_TO_Report_1` 
	FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`),
	ADD CONSTRAINT `FK_ChatRoom_TO_Report_1` 
	FOREIGN KEY (`chatroom_id`) REFERENCES `chatroom` (`chatroom_id`),
   ADD CONSTRAINT `FK_User_TO_Report_1`
   FOREIGN KEY (`reporter_user_id`) REFERENCES `user` (`user_id`),
   ADD CONSTRAINT `FK_User_TO_Report_2` 
   FOREIGN KEY (`reported_user_id`) REFERENCES `user` (`user_id`);

ALTER TABLE `review` 
	ADD CONSTRAINT `FK_book_TO_Review_1` 
	FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`),
	ADD CONSTRAINT `FK_User_TO_Review_1` 
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

ALTER TABLE `chatroom_user_info` 
	ADD CONSTRAINT `FK_ChatRoom_TO_ChatRoomUserInfo_1` 
	FOREIGN KEY (`chatroom_id`) REFERENCES `chatroom` (`chatroom_id`),
	ADD CONSTRAINT `FK_user_TO_ChatRoomUserInfo_1` 
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

ALTER TABLE `restriction_history` 
	ADD CONSTRAINT `FK_User_TO_Restriction_history_1` 
	FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
	
LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/book.csv'  	-- 입력할 파일의 경로
INTO TABLE book            -- 입력받을 테이블의 이름
FIELDS                            -- 라인 내의 필드들을 구분하는 방법
    TERMINATED BY ','              -- 각 필드가 끝나는 구분문자를 지정해줌
    ENCLOSED BY '"'                -- 구분된 필드 내에서 시작/끝 을 알리는 문자를 지정해줌
LINES                              -- 각 라인을 구분하는 방법
    TERMINATED BY '\r\n'         -- 각 라인이 끝나는 구분문자를 지정해줌
IGNORE 1 LINES                    -- 처리하지 않을 라인을 지정해줌
;

INSERT INTO `user` (`user_id`, `email`, `nickname`, `status`, `create_datetime`, `auth_provider`, `profile_img`, `withdraw_datetime`, `login_id`) VALUES
	(1, 'john.doe@example.com', 'johnny', 'ACTIVE', '2024-01-01 10:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 1),
	(2, 'jane.smith@example.com', 'janey', 'ACTIVE', '2024-01-02 11:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 2),
	(3, 'alice.johnson@example.com', 'alice', 'ACTIVE', '2024-01-03 12:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 3),
	(4, 'bob.brown@example.com', 'bobby', 'SUSPENDED', '2024-01-04 13:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 4),
	(5, 'charlie.davis@example.com', 'charlie', 'SUSPENDED', '2024-01-05 14:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 5),
	(6, 'diana.wilson@example.com', 'diana', 'ACTIVE', '2024-01-06 15:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 6),
	(7, 'edward.thompson@example.com', 'eddie', 'ACTIVE', '2024-01-07 16:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 7),
	(8, 'fiona.white@example.com', 'fi', 'PENDIN', '2024-01-08 17:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 8),
	(9, 'george.hall@example.com', 'geo', 'PENDIN', '2024-01-09 18:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 9),
	(10, 'hannah.jones@example.com', 'hanny', 'PENDIN', '2024-01-10 19:00:00', 'kakao', 'https://img.icons8.com/?size=100&id=hlS7OQRrXwiG&format=png&color=000000', NULL, 10);
INSERT INTO `chatroom` (`chatroom_id`, `book_id`, `open_datetime`, `max_member_count`, `title`, `status`) VALUES
(1, 6352228, '2024-11-01 10:00:00', 10, '고전문학 독서 모임', 'active'),
(2, 6352229, '2024-11-02 14:00:00', 10, '과학 서적 토론', 'active'),
(3, 6352230, '2024-11-03 18:30:00', 3, '역사책 함께 읽기', 'active'),
(4, 6352231, '2024-11-04 09:00:00', 8, '철학 책 읽기', 'closed'),
(5, 6352232, '2024-11-05 20:00:00', 10, '자기계발 독서 모임', 'active'),
(6, 6352233, '2024-11-06 15:00:00', 7, '소설 리뷰 모임', 'closed'),
(7, 6352234, '2024-11-07 11:00:00', 9, '에세이 함께 나눔', 'active'),
(8, 6352235, '2024-11-08 17:00:00', 5, '비문학 독서 모임', 'closed'),
(9, 6352236, '2024-11-09 13:00:00', 6, '어린이 책 읽기', 'active');
INSERT INTO `chatroom_user_info` (`chatroom_info_id`, `chatroom_id`, `user_id`, `member_join_datetime`, `chatroomUser_info_status`) VALUES
(1, 1, 1, '2024-11-01 10:00:00', 'active'),
(2, 1, 2, '2024-11-01 10:30:00', 'active'),
(3, 1, 3, '2024-11-01 11:00:00', 'active'),
(4, 1, 4, '2024-11-01 11:30:00', 'inactive'),
(5, 2, 5, '2024-11-01 12:00:00', 'active'),
(6, 2, 6, '2024-11-01 12:30:00', 'inactive'),
(7, 2, 7, '2024-11-01 13:00:00', 'active'),
(8, 3, 8, '2024-11-01 13:30:00', 'inactive'),
(9, 3, 9, '2024-11-01 14:00:00', 'active'),
(10, 3, 1, '2024-11-01 14:30:00', 'active'),
(11, 2, 2, '2024-11-01 10:00:00', 'active'),
(12, 2, 3, '2024-11-01 10:30:00', 'active'),
(13, 4, 3, '2024-11-01 11:00:00', 'active'),
(14, 4, 4, '2024-11-01 11:30:00', 'inactive'),
(15, 4, 5, '2024-11-01 12:00:00', 'active'),
(16, 4, 6, '2024-11-01 12:30:00', 'inactive'),
(17, 7, 7, '2024-11-01 13:00:00', 'active'),
(18, 7, 8, '2024-11-01 13:30:00', 'inactive'),
(19, 7, 9, '2024-11-01 14:00:00', 'active'),
(20, 7, 2, '2024-11-01 14:30:00', 'active');

INSERT INTO `review` (`review_id`, `book_id`, `user_id`, `create_datetime`, `update_datetime`, `delete_datetime`, `title`, `content`, `rating`, `status`) VALUES
	(1, 6352228, 1, '2024-11-01 10:00:00', NULL, NULL, '너에게 목소리를 보낼게 - 감동적인 이야기', '90년대생에게 보석 같은 추억을 선물해준 성우 이용신의 이야기를 잘 담아낸 책입니다. 감동적이고 따뜻합니다.', 5, 'CREATED'),
	(2, 6352229, 2, '2024-11-01 10:30:00', NULL, NULL, '일기에도 거짓말을 쓰는 사람 - 솔직한 에세이', '자의식 넘치는 차도하 시인의 첫 에세이, 솔직하고 도발적인 내용이 흥미로웠습니다.', 4, 'CREATED'),
	(3, 6352230, 3, '2024-11-01 11:00:00', NULL, NULL, '본격 한중일 세계사 12 - 역사적 통찰', '임오군란과 통킹 위기를 통해 한중일의 관계를 재조명한 흥미로운 역사서입니다.', 5, 'CREATED'),
	(4, 6352231, 4, '2024-11-01 11:30:00', NULL, NULL, '즉시 기분을 바꿔드립니다 - 유용한 처방전', '마음이 편해지고 긍정적인 기분을 유지하는 데 도움이 되는 책입니다. 추천합니다.', 4, 'CREATED'),
	(5, 6352232, 5, '2024-11-01 12:00:00', NULL, NULL, '오늘도 리추얼 - 자기 계발에 좋아요', '음악을 통한 자기만의 리추얼을 만들어 나가는 이야기가 인상적입니다.', 5, 'CREATED'),
	(6, 6352233, 6, '2024-11-01 12:30:00', NULL, NULL, '지리산 대본집 - 드라마 팬에게 추천', '드라마 <지리산>의 대본집으로, 드라마 팬이라면 소장 가치가 있습니다.', 3, 'CREATED'),
	(7, 6352234, 7, '2024-11-01 13:00:00', NULL, NULL, '달러구트 꿈 백화점 - 환상적인 이야기', '잠들어야만 입장할 수 있는 꿈 백화점의 이야기, 독특하고 상상력이 풍부한 소설입니다.', 5, 'CREATED'),
	(8, 6352235, 8, '2024-11-01 13:30:00', NULL, NULL, '그린 스완 - 새로운 경제 모델', '회복과 재생을 촉진하는 미래 경제 모델에 대한 통찰을 얻을 수 있는 책입니다.', 4, 'DELETED'),
	(9, 6352236, 9, '2024-11-01 14:00:00', NULL, NULL, '마음이 허기질 때 - 따뜻한 에세이', '저자의 따뜻한 어린 시절 음식에 대한 이야기가 마음을 울립니다.', 4, 'DELETED'),
	(10, 6352228, 10, '2024-11-01 14:30:00', NULL, NULL, '너에게 목소리를 보낼게 - 감동의 에세이', '성우 이용신의 진솔한 이야기가 인상적이었습니다. 추천합니다.', 5, 'UPDATED');

INSERT INTO `book_recommend` (`recommend_id`, `question_content`, `response_content`, `question_datetime`, `user_id`) VALUES
(1, '지금 읽기 좋은 감성적인 소설을 추천해 주세요.', '현재 계절에 어울리는 감성적인 소설로 <달러구트 꿈 백화점>을 추천드립니다. 잠들어야만 입장할 수 있는 독특한 이야기입니다.', '2024-11-01 10:00:00', 1),
(2, '비즈니스 모델을 설명해주는 경제 책이 있을까요?', '미래 경제 모델을 다룬 <그린 스완>을 추천드립니다. 회복과 재생을 촉진하는 새로운 경제 패러다임을 제시합니다.', '2024-11-01 10:30:00', 2),
(3, '일상을 편하게 해줄 자기계발서를 추천받고 싶어요.', '마음을 편하게 하고 기분을 좋게 만드는 <즉시 기분을 바꿔드립니다>를 추천합니다.', '2024-11-01 11:00:00', 3),
(4, '역사적 배경을 다룬 책 중 추천할 만한 게 있을까요?', '한중일 관계를 다룬 <본격 한중일 세계사 12>를 추천합니다. 임오군란과 통킹 위기 등을 심도 있게 다룹니다.', '2024-11-01 11:30:00', 4),
(5, '시인의 솔직한 이야기를 담은 책이 있나요?', '<일기에도 거짓말을 쓰는 사람>을 추천드립니다. 차도하 시인의 솔직하고 도발적인 이야기를 담고 있습니다.', '2024-11-01 12:00:00', 5),
(6, '음악을 통한 힐링이 가능한 책을 추천해주세요.', '<오늘도 리추얼>을 추천합니다. 음악을 통해 나다움을 찾고 힐링할 수 있는 내용입니다.', '2024-11-01 12:30:00', 6),
(7, '어린이들에게 추천할 만한 동화책이 있을까요?', '어린이들에게 상상력을 자극할 수 있는 <달러구트 꿈 백화점>을 추천합니다.', '2024-11-01 13:00:00', 7),
(8, '감동적인 이야기가 담긴 에세이를 찾고 있어요.', '성우 이용신의 이야기를 담은 <너에게 목소리를 보낼게>를 추천드립니다. 감동적인 이야기입니다.', '2024-11-01 13:30:00', 8),
(9, '드라마 <지리산> 팬이라면 읽어볼 만한 책이 있을까요?', '<지리산> 드라마의 작가판 대본집을 추천드립니다. 드라마 팬이라면 소장 가치가 있습니다.', '2024-11-01 14:00:00', 9),
(10, '음식과 추억이 담긴 따뜻한 에세이를 추천해주세요.', '<마음이 허기질 때>를 추천합니다. 음식과 함께한 따뜻한 추억을 담아낸 에세이입니다.', '2024-11-01 14:30:00', 10);




